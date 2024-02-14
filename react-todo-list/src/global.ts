

import { TaskServiceImpl } from "services/tasks/TaskServiceImpl";
import { TaskServiceStub } from "services/tasks/TaskServiceStub";

let _taskService;
if (process.env.NODE_ENV == 'development') {
    _taskService = new TaskServiceImpl();
} else {
    // TODO:
    _taskService = new TaskServiceStub();
}

export const taskService = _taskService;