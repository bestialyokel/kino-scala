import { Task, TaskStatus } from "types/task";
import { Nullable } from "utils";
import { TaskService } from "./ITaskService";
import { CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO } from "./DTO";

const BASE_URL = 'http://localhost:9000/api/v1/';
export class TaskServiceImpl implements TaskService {
    constructor() {}

    async getTasks(): Promise<Task[]> {
       const res = await fetch(BASE_URL + 'tasks')
       const tasks = await res.json();
       return tasks;
    }

    async createTask(dto: CreateTaskDTO): Promise<Task> {
        const res = await fetch(BASE_URL + 'tasks', {
            method: 'POST',
            headers: new Headers({'content-type': 'application/json'}),
            body: JSON.stringify(dto)
        });
        const task = await res.json();
        return task;
    };

    async updateTaskNameById(id: number, dto: UpdateTaskNameDTO): Promise<Nullable<Task>> {
        const res = await fetch(BASE_URL + 'tasks/' + id + '/name', {
            method: 'PATCH',
            headers: new Headers({'content-type': 'application/json'}),
            body: JSON.stringify(dto)
        });
        const task = await res.json();
        return task;
    };

    async updateTaskStatusById(id: number, dto: UpdateTaskStatusDTO): Promise<Nullable<Task>> {
        const res = await fetch(BASE_URL + 'tasks/' + id + '/status', {
            method: 'PATCH',
            headers: new Headers({'content-type': 'application/json'}),
            body: JSON.stringify(dto)
        });
        const task = await res.json();
        return task;
    };

    async deleteTaskById(id: number): Promise<Nullable<Task>> {
        const res = await fetch(BASE_URL + 'tasks/' + id, {
            method: 'DELETE',
        });
        const task = await res.json();
        return task;
    };

    async setStatusForAll(status: TaskStatus): Promise<void> {
        await fetch(BASE_URL + 'tasks/status/' + status + '/all', {
            method: 'PATCH',
        });
    };

    async deleteCompleted(): Promise<void> {
        await fetch(BASE_URL + 'tasks/delete/completed', {
            method: 'DELETE',
        });
    };
}