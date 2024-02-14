import { Task, TaskStatus } from "types/task";
import { sleep, Nullable } from "utils";
import { TaskService } from "./ITaskService";
import { CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO } from "./DTO";

const AWAIT_TIMEOUT_MS = 500;
export class TaskServiceStub implements TaskService {
    protected map: Map<number, Task> = new Map();

    constructor() {
        let task = {
            id: 0,
            name: 'abc',
            status: TaskStatus.Incompleted
        }

        for (let i = 0; i < 5; i++) {
            this.map.set(task.id, {...task});
            task.id++;
        }

    }

    async getTasks(): Promise<Task[]> {
        const tasks = this.map.values();
        
        await sleep(AWAIT_TIMEOUT_MS);
        return Array.from(tasks);
    }

    async createTask(dto: CreateTaskDTO): Promise<Task> {
        const max = Array.from(this.map.values()).reduce((acc, c) => {
            return Math.max(acc, c.id);
        }, 0);

        const task = {
            id: max + 1,
            name: dto.name,
            status: TaskStatus.Completed
        };

        this.map.set(this.map.size, task);

        await sleep(AWAIT_TIMEOUT_MS);
        return task;
    };

    async updateTaskNameById(id: number, dto: UpdateTaskNameDTO): Promise<Nullable<Task>> {
        const task = this.map.get(id);

        if (!task) {
            return null;
        }
        
        const updatedTask = {...task, name: dto.name};

        this.map.set(id, updatedTask);

        await sleep(AWAIT_TIMEOUT_MS);
        return updatedTask;
    };

    async updateTaskStatusById(id: number, dto: UpdateTaskStatusDTO): Promise<Nullable<Task>> {
        const task = this.map.get(id);

        if (!task) {
            return null;
        }
        
        const updatedTask = {...task, status: dto.status};

        this.map.set(id, updatedTask);

        await sleep(AWAIT_TIMEOUT_MS);
        return updatedTask;
    };

    async deleteTaskById(id: number): Promise<Nullable<Task>> {
        const task = this.map.get(id);
        this.map.delete(id);

        await sleep(AWAIT_TIMEOUT_MS);
        return task;
    };

    async setStatusForAll(status: TaskStatus): Promise<void> {
        const entries = this.map.entries();

        const updatedtasksEntries = Array.from(entries).map(kv => {
            const [k, v] = kv;
            return { id: k, task: {...v, status: status} };
        })

        for (const {id, task} of updatedtasksEntries) {
            this.map.set(id, task);
        }

        await sleep(AWAIT_TIMEOUT_MS);
    };

    async deleteCompleted(): Promise<void> {
        const entries = this.map.entries();
        
        type KV = {
            key: number;
            value: Task;
        };

        const [completed, incompleted] = Array.from(entries).reduce((lists, entry) => {
            const [l, r] = lists;
            const [k, v] = entry;
            const kv = {
                key: k,
                value: v
            };
            if (v.status == TaskStatus.Completed) {
                return [[...l, kv], r];
            } else {
                return [l, [...r, kv]];
            }
        }, [Array<KV>(), Array<KV>()]);

        this.map.clear();

        for (const {key, value} of completed) {
            this.map.set(key, value);
        }

        return;
    };
}