
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import { taskService } from 'global'
import { CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO } from 'services/tasks/DTO'
import { Task, TaskStatus } from 'types/task'
import { Nullable } from 'utils'

export const tasksApi = createApi({
    baseQuery: fetchBaseQuery(),
    endpoints: (build) => ({
        getAllTasks: build.query<Task[], void>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                try {
                    const tasks = await taskService.getTasks()
                    return { data: tasks }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        createTask: build.mutation<Task, CreateTaskDTO>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                try {
                    const task = await taskService.createTask(arg)
                    return { data: task }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        setTaskName: build.mutation<Nullable<Task>, {id: number, dto: UpdateTaskNameDTO}>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                const {id, dto} = arg
                try {
                    const tasks = await taskService.updateTaskNameById(id, dto)
                    return { data: tasks }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        setTaskStatus: build.mutation<Nullable<Task>, {id: number, dto: UpdateTaskStatusDTO}>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                const {id, dto} = arg
                try {
                    const tasks = await taskService.updateTaskStatusById(id, dto)
                    return { data: tasks }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        setAllTaskStatus: build.mutation<TaskStatus, TaskStatus>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                try {
                    await taskService.setStatusForAll(arg)
                    return { data: arg }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        deleteTask: build.mutation<Nullable<Task>, {id: number}>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                const {id} = arg
                try {
                    const tasks = await taskService.deleteTaskById(id)
                    return { data: tasks }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        deleteCompleted: build.mutation<void, void>({
            queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
                try {
                    await taskService.deleteCompleted()
                    return { data: void(0) }
                } catch (error) {
                    return { error: { status: 500, statusText: 'Internal Server Error', data: [] } }
                }
            },
        }),
        
    }),
})

export const { 
    useGetAllTasksQuery, 
    useCreateTaskMutation,
    useDeleteTaskMutation,
    useSetTaskNameMutation,
    useSetTaskStatusMutation,
    useDeleteCompletedMutation,
    useSetAllTaskStatusMutation
} = tasksApi