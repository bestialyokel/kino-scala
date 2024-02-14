
import { UpdateTaskNameDTO, UpdateTaskStatusDTO } from 'services/tasks/DTO';
import { Task, TaskStatus } from 'types/task';
import { Item } from '../Item/Item';
import { TasksFilter, TasksStatusFilter } from 'types/ui/task-list';

import css from './TaskItemList.module.css';


interface Props {
    tasks: Task[],
    filter: TasksFilter,
    deleteTask: (id: number) => void,
    setTaskName: (id: number, dto: UpdateTaskNameDTO) => void,
    setTaskStatus: (id: number, dto: UpdateTaskStatusDTO) => void
}

const filteredTasks = (filter: TasksFilter, tasks: Task[]) => {

    if (filter.status == TasksStatusFilter.All) {
        return tasks;
    }

    if (filter.status == TasksStatusFilter.Completed) {
        return tasks.filter(x => x.status == TaskStatus.Completed);
    }

    if (filter.status == TasksStatusFilter.Incompleted) {
        return tasks.filter(x => x.status == TaskStatus.Incompleted);
    }

    return [];
}

function TaskItemList(props: Props) {
    const {
        tasks,
        filter,
        deleteTask,
        setTaskName,
        setTaskStatus
    } = props;

    const makeDeleteHn = (task: Task) => () => {
        props.deleteTask(task.id);
    }

    const makeSetNameHn = (task: Task) => (name: string) => {
        const dto = { name };
        props.setTaskName(task.id, dto);
    }

    const makeSetStateHn = (task: Task) => (status: TaskStatus) => {
        const dto = { status };
        props.setTaskStatus(task.id, dto);
    }
    
    const visibleTasks = filteredTasks(filter, tasks);

    return (
        <div className={css['item-list-wrapper']}>
            <div className={css['item-list']}>
                <ul>
                    {visibleTasks.map(task =>
                    (
                        <li key={task.id}>
                            <Item
                                task={task}
                                setNameHn={makeSetNameHn(task)}
                                setStatusHn={makeSetStateHn(task)}
                                deleteHn={makeDeleteHn(task)}
                            />
                        </li>
                    )
                    )}
                </ul>
            </div>
        </div>
    )
}


export default TaskItemList;