
export enum TasksStatusFilter {
    All,
    Completed,
    Incompleted
}

export interface TasksFilter {
    status: TasksStatusFilter
}

export enum TaskListUIState {
    None,
    Loading,
    Display,
    Error
}