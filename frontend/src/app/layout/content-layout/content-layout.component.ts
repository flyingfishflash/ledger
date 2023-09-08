// angular
import { Component } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import { HeadingComponent } from '../heading/heading.component'

// core and shared
//import { ThemeService } from '@core/services/theme.service';
//import { themes } from '@core/constants/themes';

@Component({
  selector: 'app-content-layout',
  templateUrl: './content-layout.component.html',
  styleUrls: ['./content-layout.component.scss'],
  standalone: true,
  imports: [HeadingComponent, RouterOutlet],
})
export class ContentLayoutComponent {
  currentTheme: string = ''

  //private overlayContainer: OverlayContainer

  /*   currentActiveTheme$ = this.themeService.getDarkTheme().pipe(
    map((isDarkTheme: boolean) => {
      const [lightTheme, darkTheme] = themes;

      this.currentTheme = isDarkTheme ? lightTheme.name : darkTheme.name;

      if (this.overlayContainer) {
        const overlayContainerClasses = this.overlayContainer.getContainerElement()
          .classList;
        const themeClassesToRemove = Array.from(
          overlayContainerClasses
        ).filter((item: string) => item.includes("-theme"));
        if (themeClassesToRemove.length) {
          overlayContainerClasses.remove(...themeClassesToRemove);
        }
        overlayContainerClasses.add(this.currentTheme);
      }

      return this.currentTheme;
    })
  );
 */

  // constructor(private themeService: ThemeService) {}

  // ngOnInit(): void {
  //     if (this.overlayContainer) {
  //     this.overlayContainer
  //       .getContainerElement()
  //       .classList.add(this.currentTheme);
  //   }
  // }
}
