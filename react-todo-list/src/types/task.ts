export interface Task {
    id: number;
    name: string;
    status: TaskStatus;
    deleted?: boolean;
}

export enum TaskStatus {
    Completed = 'completed',
    Incompleted = 'incompleted'
}