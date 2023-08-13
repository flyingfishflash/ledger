// modules (angular)
import { APP_INITIALIZER, NgModule } from '@angular/core'
import { BrowserModule } from '@angular/platform-browser'
import { HttpClientModule } from '@angular/common/http'
import { NoopAnimationsModule } from '@angular/platform-browser/animations'

import { Observable } from 'rxjs'

// modules
import { AppRoutingModule } from './app-routing.module'
import { CoreModule } from './core/core.module'
import { SharedModule } from './shared/shared.module'
import { LoginModule } from './modules/login/login.module'

// components
import { AppComponent } from './app.component'
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component'
import { ContentLayoutComponent } from './layout/content-layout/content-layout.component'
import { ErrorLayoutComponent } from './layout/error-layout/error-layout.component'
import { HeadingComponent } from './layout/heading/heading.component'

import { AppConfigRuntime } from './app-config-runtime'

const appInitializerFn = (
  config: AppConfigRuntime,
): (() => Observable<AppConfigRuntime>) => {
  return () => config.load()
}

@NgModule({
  bootstrap: [AppComponent],

  declarations: [
    AppComponent,
    AuthLayoutComponent,
    ContentLayoutComponent,
    ErrorLayoutComponent,
    HeadingComponent,
  ],

  exports: [],

  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    CoreModule,
    LoginModule,
    NoopAnimationsModule,
    SharedModule,
  ],

  providers: [
    AppConfigRuntime,
    {
      provide: APP_INITIALIZER,
      useFactory: appInitializerFn,
      deps: [AppConfigRuntime],
      multi: true,
    },
  ],
})
export class AppModule {}
