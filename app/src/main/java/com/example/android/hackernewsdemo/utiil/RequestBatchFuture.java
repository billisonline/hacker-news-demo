package com.example.android.hackernewsdemo.utiil;

import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestBatchFuture<T> implements Future<List<T>>, Response.Listener<T>, Response.ErrorListener {
    private List<Request<T>> mRequests;
    private final List<T> mResults = new ArrayList<>();
    private VolleyError mException;

    public static <E> RequestBatchFuture<E> newFuture() {
        return new RequestBatchFuture<>();
    }

    private RequestBatchFuture() {}

    public void setRequests(List<Request<T>> requests) {
        mRequests = requests;
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (mRequests == null) {
            return false;
        }

        if (!isDone()) {
            for (Request<?> request : mRequests) {
                request.cancel();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<T> get() throws InterruptedException, ExecutionException {
        try {
            return doGet(/* timeoutMs= */ null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public List<T> get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    private synchronized List<T> doGet(Long timeoutMs)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (mException != null) {
            throw new ExecutionException(mException);
        }

        if (allResultsReceived()) {
            return mResults;
        }

        if (timeoutMs == null) {
            while (!isDone()) {
                wait(0);
            }
        } else if (timeoutMs > 0) {
            long nowMs = SystemClock.uptimeMillis();
            long deadlineMs = nowMs + timeoutMs;
            while (!isDone() && nowMs < deadlineMs) {
                wait(deadlineMs - nowMs);
                nowMs = SystemClock.uptimeMillis();
            }
        }

        if (mException != null) {
            throw new ExecutionException(mException);
        }

        if (!allResultsReceived()) {
            throw new TimeoutException();
        }

        return mResults;
    }

    @Override
    public boolean isCancelled() {
        if (mRequests == null) {
            return false;
        }

        for (Request<T> request : mRequests) {
            if (!request.isCanceled()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public synchronized boolean isDone() {
        return allResultsReceived() || mException != null || isCancelled();
    }

    @Override
    public synchronized void onResponse(T response) {
        mResults.add(response);

        if (mResults.size() < mRequests.size()) {
            Log.w("HackerNewsApiRepository", mResults.size() + " responses so far");
            //
        } else if (mResults.size() == mRequests.size()) {
            notifyAll();
        } else {
            throw new RuntimeException();
        }
    }

    private boolean allResultsReceived() {
        return mResults.size() == mRequests.size();
    }

    @Override
    public synchronized void onErrorResponse(VolleyError error) {
        mException = error;
        notifyAll();
    }
}
