import { TodoService } from './../../services/todo.service';
import { Idea } from './../../models/idea.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-idea',
  templateUrl: './add-idea.component.html',
  styleUrl: './add-idea.component.css'
})

export class AddIdeaComponent {
  constructor(service: TodoService) {

  }
  submitted: boolean = false;
  idea: Idea = {
    name:'',
      description:'',
      completed: false
  }

  newIdea() {
    this.submitted = false;
    this.idea = {
      name:'',
      description:'',
      completed: false
    }
  }

  addIdea() {
    const data = {
      name: this.idea.name,
      desciption: this.idea.description
    }
    console.log(data)
    this.submitted = true; // get values from service and say submitted

    //throw new Error('Method not implemented.');
  }

}
