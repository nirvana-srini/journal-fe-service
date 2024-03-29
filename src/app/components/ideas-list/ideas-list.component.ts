import { Idea } from './../../models/idea.model';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-ideas-list',
  templateUrl: './ideas-list.component.html',
  styleUrl: './ideas-list.component.css'
})
export class IdeasListComponent implements OnInit{
  currentIndex = -1;
  searchString:string = '';
  currentIdea?:Idea;

  ideas?: Idea[] = [
    {
      name: 'Default',
      description: 'Default'
    }
  ]

  ngOnInit(): void {
   this.getIdeas()
  }

  searchByName(): any  {
    console.log(this.searchString)
    return this.ideas;
  }

  getIdeas() {
    return this.ideas;
  }

  setCurrentIdea(idea:Idea, currentIndex:number){
    this.currentIdea=idea;
    this.currentIndex = currentIndex;
    console.log(currentIndex + ":" + this.currentIdea.name)
  }

  removeAll() {

  }

}

