import { TodoService } from './../../services/todo.service';
import { Component, Input, OnInit  } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Todo } from '../../models/todo.model';

@Component({
  selector: 'app-todo-details',
  templateUrl: './todo-details.component.html',
  styleUrl: './todo-details.component.css'
})
export class TodoDetailsComponent implements OnInit {

  @Input() viewMode = false;

  @Input() currentTodo: Todo = {
    name: '',
    description: '',
    completed: false
  };

  message = '';

  constructor(
    private todoService:TodoService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    console.log("this.currentTodo.id: "+ this.currentTodo.id)
    if (!this.viewMode) {
      this.message = '';
      console.log("InviewMode"+ this.route.snapshot.params["id"])
      this.getTodo(this.route.snapshot.params["id"]);
    }
  }

  getTodo(id: string): void {
    console.log("getTodo id"+ id)
    this.todoService.get(id)
      .subscribe({
        next: (data) => {
          this.currentTodo = data;
          console.log("getTodo::"+ data);
        },
        error: (e) => console.error(e)
      });
  }

  updateCompleted(status: boolean): void {
    const data = {
      name: this.currentTodo.name,
      description: this.currentTodo.description,
      published: status
    };

    this.message = '';

    this.todoService.update(this.currentTodo.id, data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.currentTodo.completed = status;
          //this.message = res.message ? res.message : 'The status was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }

  updateTodo(): void {
    this.message = '';

    this.todoService.update(this.currentTodo.id, this.currentTodo)
      .subscribe({
        next: (res) => {
          console.log(res);
          //this.message = res.message ? res.message : 'The status was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }

  deleteTodo(): void {
    this.todoService.delete(this.currentTodo.id)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.router.navigate(['/todos']);
        },
        error: (e) => console.error(e)
      });
  }

}
