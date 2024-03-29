import { Todo } from '../../models/todo.model';
import { TodoService } from './../../services/todo.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-todos-list',
  templateUrl: './todos-list.component.html',
  styleUrl: './todos-list.component.css'
})
export class TodosListComponent implements OnInit{
  name = '';
  currentIndex = -1;
  todos?: Todo[];
  currentTodo: Todo = {};

  constructor(private service:TodoService) {

  }

  ngOnInit(): void {
    this.getTodos()
  }

  getTodos() {
    this.service.getAll()
    .subscribe({
      next: (data) => {
        this.todos = data;
        console.log(data);
      },
      error: (e) => console.error(e)
    })
  }
  refreshList(): void {
    this.getTodos();
    this.currentTodo = {};
    this.currentIndex = -1;
  }
  setActiveTodo(todo: Todo, index: number): void {
    this.currentTodo = todo;
    this.currentIndex = index;
  }

  removeAllTodos(): void {
    this.service.deleteAll()
      .subscribe({
        next: (res) => {
          console.log(res);
        },
        error: (e) => console.error(e)
      });
      this.refreshList();
  }

  searchByName(): void {
    this.currentTodo = {};
    this.currentIndex = -1;

    this.service.findByName(this.name)
      .subscribe({
        next: (data) => {
          this.todos = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }
}


