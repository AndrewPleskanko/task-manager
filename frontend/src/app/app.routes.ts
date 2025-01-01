import {Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {RegistrationComponent} from "./registration/registration.component";
import {LoginComponent} from "./login/login.component";
import {TokenHandlerComponent} from "./auth/token-handler/token-handler.component";
import {TaskBoardComponent} from "./task-board/task-board.component";
import {SettingsComponent} from "./pages/settings/settings.component";
import {TeamsComponent} from "./pages/teams/teams.component";
import {NotificationsComponent} from "./pages/notifications/notifications.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'home', component: HomeComponent},
  {path: 'token-handler', component: TokenHandlerComponent},
  {path: 'login', component: LoginComponent},
  {path: 'task-board', component: TaskBoardComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'notifications', component: NotificationsComponent},
  {path: 'teams', component: TeamsComponent},
];

export class AppRoutingModule {
}
