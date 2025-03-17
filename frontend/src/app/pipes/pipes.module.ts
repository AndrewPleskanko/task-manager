import {NgModule} from '@angular/core';
import {RolePipe} from './role.pipe';
import {StatusPipe} from "./status.pipe";

@NgModule({
  declarations: [RolePipe, StatusPipe],
  exports: [RolePipe, StatusPipe],
})
export class PipesModule {
}
