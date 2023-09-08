// angular
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'
import {
  APP_INITIALIZER,
  enableProdMode,
  importProvidersFrom,
} from '@angular/core'
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser'
import { provideNoopAnimations } from '@angular/platform-browser/animations'

// third party
import { Observable } from 'rxjs'

// shared modules
import { AppConfigRuntime } from 'src/app/app-config-runtime'
import { AppRoutingModule } from 'src/app/app-routing.module'
import { AppComponent } from 'src/app/app.component'
import { CoreModule } from 'src/app/core/core.module'
import { LoginModule } from 'src/app/modules/login/login.module'
import { SharedModule } from 'src/app/shared/shared.module'
import { environment } from 'src/environments/environment'

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
    importProvidersFrom(
      AppRoutingModule,
      BrowserModule,
      CoreModule,
      LoginModule,
      SharedModule,
    ),
    AppConfigRuntime,
    {
      provide: APP_INITIALIZER,
      useFactory: appInitializerFn,
      deps: [AppConfigRuntime],
      multi: true,
    },
    provideHttpClient(withInterceptorsFromDi()),
    provideNoopAnimations(),
  ],
}).catch((err) => console.error(err))
