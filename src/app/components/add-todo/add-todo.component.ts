import { Component } from '@angular/core';
import { Todo } from '../../models/todo.model';
import { TodoService } from '../../services/todo.service';

@Component({
  selector: 'app-add-todo',
  templateUrl: './add-todo.component.html',
  styleUrl: './add-todo.component.css',
})

export class AddTodoComponent {
  submitted: boolean = false;

  todo: Todo = {
    name : '',
    description: '',
    completed: false
  };

  constructor(private todoService: TodoService){

  }

  newTodo(): void {
    this.submitted = false;
    this.todo = {
      name: '',
      description: '',
      completed: false
    };
  }

  saveTodo() {
    const data = {
      name: this.todo.name,
      description: this.todo.description,
      completed: false
    }
    console.log('Saving Todo'+ data.name + data.completed)
    this.todoService.create(data)
    .subscribe({
      next: (res) => {
        console.log(res);
        this.submitted=true;
      },
      error: (e) => console.error(e)
    })
  }

}
