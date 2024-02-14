import { AsyncThunk, UnknownAction } from "@reduxjs/toolkit";

export type Nullable<T> = T | null | undefined;

export function sleep(ms: number) {
    return new Promise((resolve) => {
        setTimeout(() => resolve(null), ms);
    });
}

export type GenericAsyncThunk = AsyncThunk<unknown, unknown, any>

export type PendingAction = ReturnType<GenericAsyncThunk['pending']>
export type RejectedAction = ReturnType<GenericAsyncThunk['rejected']>
export type FulfilledAction = ReturnType<GenericAsyncThunk['fulfilled']>
export type SettledAction = ReturnType<GenericAsyncThunk['settled']>

export function isPendingAction(action: UnknownAction): action is PendingAction {
  return typeof action.type === 'string' && action.type.endsWith('/pending')
}