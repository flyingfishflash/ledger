import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http'
import {
  APP_INITIALIZER,
  enableProdMode,
  importProvidersFrom,
} from '@angular/core'
import { MatDialogModule } from '@angular/material/dialog'
import { bootstrapApplication } from '@angular/platform-browser'
import { provideNoopAnimations } from '@angular/platform-browser/animations'
import { Routes, provideRouter } from '@angular/router'
import { Observable } from 'rxjs'
import { AppConfigRuntime } from './app/app-config-runtime'
import { AppComponent } from './app/app.component'
import { AuthInterceptor } from './app/core/authentication/basic-auth.interceptor'
import { GlobalErrorHandler } from './app/core/error-handling/global-error-handler'
import { HttpErrorInterceptor } from './app/core/error-handling/http-error.interceptor'
import { environment } from './environments/environment'

const routes: Routes = [
  {
    path: 'accounts-table',
    loadComponent: () =>
      import(
        './app/domain/accounts/accounts-table/accounts-table.component'
      ).then((m) => m.AccountsTableComponent),
  },
  {
    path: 'admin/settings',
    loadComponent: () =>
      import(
        './app/domain/general/admin-settings/admin-settings.component'
      ).then((m) => m.AdminSettingsComponent),
  },
  {
    path: 'admin/settings/user/create',
    loadComponent: () =>
      import(
        './app/domain/general/admin-settings-user-create/admin-settings-user-create.component'
      ).then((m) => m.AdminSettingsUserCreateComponent),
  },
  {
    path: 'home',
    loadComponent: () =>
      import('./app/domain/general/home/home.component').then(
        (m) => m.HomeComponent,
      ),
  },
  {
    path: 'import',
    loadComponent: () =>
      import('./app/domain/import/import.component').then(
        (m) => m.ImportComponent,
      ),
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('./app/domain/general/profile/profile.component').then(
        (m) => m.ProfileComponent,
      ),
  },
  {
    path: 'error',
    loadComponent: () =>
      import('./app/core/error/error.component').then((m) => m.ErrorComponent),
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./app/domain/general/login/login.component').then(
        (m) => m.LoginComponent,
      ),
  },
  // { path: '**', redirectTo: '/login' },
  // { path: '**', component: PageNotFoundComponent },
]

const appInitializerFn = (
  config: AppConfigRuntime,
): (() => Observable<AppConfigRuntime>) => {
  return () => config.load()
}

if (environment.production) {
  enableProdMode()
}

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    // someday MatDialog will be standalone and this will be unnecessary
    // https://github.com/angular/components/issues/26124
    importProvidersFrom([MatDialogModule]),
    AppConfigRuntime,
    {
      provide: APP_INITIALIZER,
      useFactory: appInitializerFn,
      deps: [AppConfigRuntime],
      multi: true,
    },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true },
    GlobalErrorHandler,
    provideHttpClient(withInterceptorsFromDi()),
    provideNoopAnimations(),
  ],
}).catch((err) => console.error(err))
