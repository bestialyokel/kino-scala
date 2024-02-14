import { useEffect, useRef, useState } from "react";
import {ReactComponent as DeleteIcon} from './x-close-delete.svg';
import { Task, TaskStatus } from "types/task";

import css from './Item.module.css';

interface Props {
    task: Task
    deleteHn: () => void
    setNameHn: (name: string) => void
    setStatusHn: (status: TaskStatus) => void
}

interface State {
    name: string
    editNameMode: boolean
}

function initialState(props: Props): State {
    return {
        name: props.task.name,
        editNameMode: false
    }
}

export function Item(props: Props) {
    const {
        deleteHn,
        setNameHn,
        setStatusHn
    } = props;

    const [state, setState] = useState(initialState(props));
    const containerRef = useRef<HTMLDivElement>(null);

    const nameChangeHn = (e: React.FormEvent<HTMLInputElement>) => {
        setState({...state, name: e.currentTarget.value});
    }

    const statusChangeHn = (e: React.FormEvent<HTMLInputElement>) => {
        const v = e.currentTarget.checked;
        const status = v ? TaskStatus.Completed : TaskStatus.Incompleted;
        setStatusHn(status);
    }

    const makeEditModeHn = (edit: boolean) => () => {
        setState({...state, editNameMode: edit});
    }

    const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.code == "Enter") {
            setState({...state, editNameMode: false});
            setNameHn(state.name);
        }
    }

    useEffect(() => {
        const clickOutsideHn = (event: MouseEvent) => {
            if (containerRef.current?.contains(event.target as HTMLElement)) {
                return;
            }

            setState({...state, editNameMode: false});
        }

        document.addEventListener("mousedown", clickOutsideHn);
        return () => {
            document.removeEventListener("mousedown", clickOutsideHn);
        }
    }, [containerRef, props]);    

    let task;
    if (!state.editNameMode) {
        task = (
            <div className={css['task']} onDoubleClick={makeEditModeHn(true)}>
                <div className={css['checkbox']}>
                    <input type="checkbox" 
                    checked={props.task.status == TaskStatus.Completed}
                    onChange={statusChangeHn}/>
                </div>
                <span>{state.name}</span>
                <button className={css['delete-btn']} onClick={deleteHn}>
                    <DeleteIcon height={20} width={20}/>
                </button>
            </div>
        );
    } else {
        task = (
            <div className={css['task']} ref={containerRef}>
                <input
                    type="text"
                    value={state.name}
                    autoFocus
                    onChange={nameChangeHn}
                    onKeyDown={onKeyDown}
                    />
            </div>
        )
    }
    return (
        <div className={css['task-wrapper']}>
            {task}
        </div>
    )
}