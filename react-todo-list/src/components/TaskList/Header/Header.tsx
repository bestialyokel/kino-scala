
import { Task, TaskStatus } from 'types/task';
import { CreateTaskDTO } from 'services/tasks/DTO';
import { useState } from 'react';

import css from './Header.module.css';

interface Props {
    tasks: Task[]
    setStatusForAll: (status: TaskStatus) => void
    createTask: (dto: CreateTaskDTO) => void
}

interface State {
    name: string
}

function initialState(props: Props): State {
    return {
        name: ''
    }
}

export function Header(props: Props) {
    const {
        tasks,
        createTask,
        setStatusForAll
    } = props

    const [state, setState] = useState(initialState(props))

    const allTasksCompleted = tasks.every(x => x.status == TaskStatus.Completed)

    const nameChangeHn = (e: React.FormEvent<HTMLInputElement>) => {
        setState({...state, name: e.currentTarget.value})
    }

    const createTaskHn = () => {
        createTask({ name: state.name })
        setState({...state, name: ''})
    }

    const toggleStatusHn = () => {
        const status = allTasksCompleted 
            ? TaskStatus.Incompleted 
            : TaskStatus.Completed;

        setStatusForAll(status);
    }

    const taskNameKeydownHn = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.code == "Enter" && state.name.length > 0) {
            createTaskHn()
        }
    }

    return (
        <div className={css['header-Wrapper']}>
            <div className={css['header']}>
                <div className={css['toggle-completed']}>
                    <button onClick={toggleStatusHn}>V</button>
                </div>
                <div className={css['task-name-input']}>
                    <input type="text" value={state.name} onKeyDown={taskNameKeydownHn} onChange={nameChangeHn}/>
                </div>
            </div>
        </div>
    )
}