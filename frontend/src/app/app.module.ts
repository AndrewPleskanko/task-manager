import {BrowserModule} from '@angular/platform-browser';
import {NgModule, importProvidersFrom} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastrModule} from 'ngx-toastr';
import {AppRoutingModule} from './app.routes';
import {RouterModule} from '@angular/router';
import {NgxChartsModule} from '@swimlane/ngx-charts';

@NgModule({
  imports: [
    BrowserModule,
    NgxChartsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([]),
    NgbModule,
    DragDropModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot()
  ],
  providers: [
    importProvidersFrom(AppRoutingModule)
  ]
})
export class AppModule {
}
