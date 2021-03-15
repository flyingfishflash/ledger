// angular
import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "padWithSpaces",
})
export class PadWithSpacesPipe implements PipeTransform {
  transform(value: string, characterCount: number): string {
    return value.padStart(value.length + characterCount + 3, "  ");
  }
}
