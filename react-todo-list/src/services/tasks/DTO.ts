import { TaskStatus } from "types/task";

export interface CreateTaskDTO {
    name: string
}

export interface UpdateTaskNameDTO {
    name: string
}

export interface UpdateTaskStatusDTO {
    status: TaskStatus
}