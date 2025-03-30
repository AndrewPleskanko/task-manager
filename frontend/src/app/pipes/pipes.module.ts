import {NgModule} from '@angular/core';
import {RolePipe} from './role.pipe';
import {StatusPipe} from "./status.pipe";
import {PriorityPipe} from "./priority.pipe";

@NgModule({
  declarations: [RolePipe, StatusPipe, PriorityPipe],
  exports: [RolePipe, StatusPipe, PriorityPipe],
})
export class PipesModule {
}
