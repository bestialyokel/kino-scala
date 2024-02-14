
import { TaskListState } from 'store/features/tasks/reducer';
import { CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO } from 'services/tasks/DTO';
import { Task, TaskStatus } from 'types/task';
import { ConnectedProps, connect } from 'react-redux';
import { Header } from './Header/Header';
import TaskItemList from './TaskItemList/TaskItemList';
import { Footer } from './Footer/Footer';
import { TasksFilter } from 'types/ui/task-list';
import { setTasks, setTasksFilter } from 'store/features/tasks/syncActions';

import { useCreateTaskMutation, useDeleteCompletedMutation, useDeleteTaskMutation, useGetAllTasksQuery, useSetAllTaskStatusMutation, useSetTaskNameMutation, useSetTaskStatusMutation } from 'store/features/tasks/rtk';
import { Spinner } from 'components/ui/spinner/Spinner';
import { useEffect } from 'react';

import css from './TaskList.module.css';

// todo - infer type in store.ts and export
interface RootState {
    tasks: TaskListState
}

const mapState = (state: RootState) => {
    return ({
        tasks: state.tasks.tasks,
        filter: state.tasks.filter
    })
}

const mapDispatch = {
    setTasks: (tasks: Task[]) => setTasks(tasks),
    setFilter: (filter: TasksFilter) => setTasksFilter(filter)
}

const connector = connect(mapState, mapDispatch)

type PropsFromRedux = ConnectedProps<typeof connector>

interface Props extends PropsFromRedux { }

function TaskList(props: Props) {
    const {
        setFilter,
        setTasks,
        tasks,
        filter
    } = props

    const { data, isFetching } = useGetAllTasksQuery();
    const [ createTaskRtk, createFlags ] = useCreateTaskMutation();
    const [ setTaskNameRtk, setNameFlags ] = useSetTaskNameMutation();
    const [ setTaskStatusRtk, setStatusFlags ] = useSetTaskStatusMutation();
    const [ setTaskStatusAllRtk, setStatusAllFlags ] = useSetAllTaskStatusMutation();
    const [ deleteTaskRtk, deleteFlags ] = useDeleteTaskMutation();
    const [ deleteCompletedRtk, deleteCompletedFlags ] = useDeleteCompletedMutation();

    const createTask = async (dto: CreateTaskDTO) => {
        await createTaskRtk(dto);
    }

    const deleteTask = async (id: number) => {
        await deleteTaskRtk({id});
    }

    const setTaskName = async (id: number, dto: UpdateTaskNameDTO) => {
        await setTaskNameRtk({id, dto});
    }

    const setTaskStatus = async (id: number, dto: UpdateTaskStatusDTO) => {
        await setTaskStatusRtk({id, dto});
    }

    const setStatusForAll = async (status: TaskStatus) => {
        await setTaskStatusAllRtk(status);
    }

    const deleteCompleted = async () => {
        await deleteCompletedRtk();
    }

    useEffect(() => {
        const tasks = data || [];
        setTasks(tasks);
    }, [data]);

    const headerLoading = [createFlags, setStatusAllFlags].some(x => x.isLoading === true);
    const listLoading = [deleteFlags, 
        setNameFlags,
        setStatusFlags, 
        setStatusAllFlags].some(x => x.isLoading === true);
    const footerLoading = [deleteCompletedFlags, deleteFlags].some(x => x.isLoading === true);

    const isLoading = headerLoading || listLoading || footerLoading;

    if (isLoading) {
        return (
            <Spinner/>
        )
    }

    return (
        <div className={css['task-list']}>
            <Header tasks={tasks} createTask={createTask} setStatusForAll={setStatusForAll}/>
            <TaskItemList
                tasks={tasks}
                filter={filter}
                deleteTask={deleteTask}
                setTaskName={setTaskName}
                setTaskStatus={setTaskStatus}
            />
            <Footer tasks={tasks} 
                filter={filter}
                setFilter={setFilter}
                deleteCompleted={deleteCompleted}/>
        </div>
    )
}


export default connector(TaskList);