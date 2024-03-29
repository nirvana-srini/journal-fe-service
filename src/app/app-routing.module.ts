import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TodosListComponent } from './components/todos-list/todos-list.component';
import { AddTodoComponent } from './components/add-todo/add-todo.component';
import { TodoDetailsComponent } from './components/todo-details/todo-details.component';
import { AddIdeaComponent } from './components/add-idea/add-idea.component';
import { IdeasListComponent } from './components/ideas-list/ideas-list.component';
import { IdeasDetailsComponent } from './components/idea-details/idea-details.component';

const routes: Routes = [
  { path: '', redirectTo: 'todos', pathMatch: 'full' },
  { path: 'todos', component: TodosListComponent },
  { path: 'todos/:id', component: TodoDetailsComponent },
  { path: 'add', component: AddTodoComponent },
  { path: 'add-idea', component: AddIdeaComponent },
  { path: 'ideas', component: IdeasListComponent },
  { path: 'ideas/:id', component: IdeasDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
