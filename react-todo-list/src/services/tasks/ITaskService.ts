import { Task, TaskStatus } from "types/task";
import { Nullable } from "utils";
import { CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO } from "./DTO";

export interface TaskService {
    getTasks(): Promise<Task[]>
    createTask(dto: CreateTaskDTO): Promise<Task>
    updateTaskNameById(id: number, dto: UpdateTaskNameDTO): Promise<Nullable<Task>>
    updateTaskStatusById(id: number, dto: UpdateTaskStatusDTO): Promise<Nullable<Task>>
    setStatusForAll(status: TaskStatus): Promise<void>
    deleteTaskById(id: number): Promise<Nullable<Task>>
    deleteCompleted(): Promise<void>
}