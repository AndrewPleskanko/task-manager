import {Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./auth/login/login.component";
import {TokenHandlerComponent} from "./auth/token-handler/token-handler.component";
import {TaskBoardComponent} from "./pages/task-board/task-board.component";
import {SettingsComponent} from "./pages/settings/settings.component";
import {TeamsComponent} from "./pages/teams/teams.component";
import {NotificationsComponent} from "./pages/notifications/notifications.component";
import {ChartsComponent} from "./pages/charts/charts.component";
import {AiComponent} from "./pages/ai/ai.component";
import {CalendarComponent} from "./pages/calendar/calendar.component";
import {ProjectsComponent} from "./pages/projects/projects.component";
import {TasksComponent} from "./pages/tasks/tasks.component";
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {UserProfileComponent} from "./pages/user-profile/user-profile.component";
import {AdminComponent} from "./pages/admin/admin.component";
import {TagsComponent} from "./pages/admin/tags/tags.component";
import {UserStoriesComponent} from "./pages/user-stories/user-stories.component";
import {TaskDetailsComponent} from "./pages/task-details/task-details.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'token-handler', component: TokenHandlerComponent},
  {path: 'login', component: LoginComponent},
  {path: 'task-board', component: TaskBoardComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'notifications', component: NotificationsComponent},
  {path: 'teams', component: TeamsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'tasks', component: TasksComponent},
  {path: 'projects', component: ProjectsComponent},
  {path: 'calendar', component: CalendarComponent},
  {path: 'ai', component: AiComponent},
  {path: 'charts', component: ChartsComponent},
  {path: 'profile/:id', component: UserProfileComponent},
  {path: 'user-stories', component: UserStoriesComponent},
  {path: 'task-details', component: TaskDetailsComponent},
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      {path: 'tasks', component: TasksComponent},
      {path: 'tags', component: TagsComponent},
      {path: 'settings', component: SettingsComponent},
      {path: '', redirectTo: 'users', pathMatch: 'full'},
    ],
  },
  {path: '**', redirectTo: 'home'},
];

export class AppRoutingModule {
}
