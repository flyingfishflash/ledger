// angular
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'
import { NgModule, Optional, SkipSelf } from '@angular/core'

// core
import { AuthInterceptor } from './authentication/basic-auth.interceptor'
import { ErrorHandlerModule } from './errors/error-handler.module'
import { AuthGuard } from './guards/auth.guard'
import { StorageService } from './storage/storage.service'
import { ValidationService } from './validation/validation.service'

const throwIfAlreadyLoadedFn = (parentModule: any, moduleName: string) => {
  if (parentModule) {
    throw new Error(
      `${moduleName} has already been loaded. Import ${moduleName} modules in the AppModule only.`,
    )
  }
}

@NgModule({
  declarations: [],
  imports: [ErrorHandlerModule, HttpClientModule],
  providers: [
    AuthGuard,
    //NoAuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    StorageService,
    ValidationService,
  ],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoadedFn(parentModule, 'CoreModule')
  }
}
