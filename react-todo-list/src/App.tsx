import './App.css';

import 'normalize.css';
import TaskList from './components/TaskList/TaskList';

function App() {
  return (
    <div className="App">
      <div className="task-list">
        <TaskList></TaskList>
      </div>
    </div>
  );
}

export default App;
