import { Task } from 'types/task';
import { TasksFilter, TasksStatusFilter } from 'types/ui/task-list';

import css from './Footer.module.css';

interface Props {
    tasks: Task[],
    filter: TasksFilter
    deleteCompleted: () => void
    setFilter: (filter: TasksFilter) => void
}

interface State {}

function initialState(props: Props): State {
    return {}
}

export function Footer(props: Props) {
    const {
        tasks,
        deleteCompleted,
        setFilter,
        filter
    } = props

    const itemsQuantity = tasks.length == 1 ? 'item' : 'items'
    const itemsLeftText = `${tasks.length} ${itemsQuantity} left!`

    const setStatusFilter = (status: TasksStatusFilter) => {
        setFilter({...filter, status })
    }

    return (
        <div className={css['footer-wrapper']}>
            <div className={css['footer']}>
                <div className={css['counter']}>
                    <span>{itemsLeftText}</span>
                </div>
                <div className={css['filter']}>
                    <button onClick={() => setStatusFilter(TasksStatusFilter.All)}>
                        All
                    </button>
                    <button onClick={() => setStatusFilter(TasksStatusFilter.Incompleted)}>
                        Active
                    </button>
                    <button onClick={() => setStatusFilter(TasksStatusFilter.Completed)}>
                        Completed
                    </button>
                </div>
                <div className={css['clear']}>
                    <button onClick={() => deleteCompleted()}>
                        Clear completed
                    </button>
                </div>
            </div>
        </div>
    )
}