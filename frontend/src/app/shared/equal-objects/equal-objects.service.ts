// angular
import { Injectable } from "@angular/core";

// core and shared
import { Logger } from "@core/logging/logger.service";

const log = new Logger("equal-objects.service");

export interface ObjectEqualityState {
  equal: boolean;
  differences: string[];
}

@Injectable({
  providedIn: "root",
})
export class EqualObjectsService {
  isEqual(value, other): ObjectEqualityState {
    const myReturn: ObjectEqualityState = { equal: true, differences: [] };
    // log.debug(myReturn);

    // Get the value type
    const type = Object.prototype.toString.call(value);
    // let objName = Object.prototype.toString.call(other);

    /*
      log.debug(type);
      log.debug(objName);
      log.debug(Object.getOwnPropertyNames(value));
    */

    // If the two objects are not the same type, return false
    if (type !== Object.prototype.toString.call(other)) {
      myReturn.equal = false;
      // log.debug(myReturn);
      return myReturn;
    }

    // If items are not an object or array, return false
    if (["[object Array]", "[object Object]"].indexOf(type) < 0) {
      myReturn.equal = false;
      // log.debug(myReturn);
      return myReturn;
    }

    // Compare the length of the length of the two items
    const valueLen =
      type === "[object Array]" ? value.length : Object.keys(value).length;
    const otherLen =
      type === "[object Array]" ? other.length : Object.keys(other).length;
    if (valueLen !== otherLen) {
      myReturn.equal = false;
      // log.debug(myReturn);
      return myReturn;
    }

    // Compare two items
    const compare = (item1, item2) => {
      // log.debug(item1, item2);

      // Get the object type
      const itemType = Object.prototype.toString.call(item1);

      // If an object or array, compare recursively
      if (["[object Array]", "[object Object]"].indexOf(itemType) >= 0) {
        if (!this.isEqual(item1, item2)) {
          myReturn.equal = false;
          // log.debug(myReturn);
          // return myReturn.equal;
        }
      }

      // Otherwise, do a simple comparison
      else {
        // If the two items are not the same type, return false
        if (itemType !== Object.prototype.toString.call(item2)) {
          myReturn.equal = false;
          // log.debug(myReturn);
          // return myReturn.equal;
        }

        // Else if it's a function, convert to a string and compare
        // Otherwise, just compare
        if (itemType === "[object Function]") {
          if (item1.toString() !== item2.toString()) {
            myReturn.equal = false;
            // log.debug(myReturn);
            return myReturn.equal;
          }
        } else {
          if (item1 !== item2) {
            myReturn.equal = false;
            // log.debug(myReturn);
            return myReturn.equal;
          }
        }
      }
    }; // compare function

    // Compare properties
    if (type === "[object Array]") {
      for (let i = 0; i < valueLen; i++) {
        if (compare(value[i], other[i]) === false) {
          myReturn.equal = false;
          // log.debug(myReturn);
          return myReturn;
        }
      }
    } else {
      for (const key in value) {
        if (Object.prototype.hasOwnProperty.call(value, key)) {
          // log.debug(key);
          if (compare(value[key], other[key]) === false) {
            myReturn.equal = false;
            myReturn.differences.push(key);
            // log.debug(myReturn);
            // return myReturn;
          }
        }
      }
    }

    // If nothing failed, return true
    {
      // myReturn.equal = true;
      // log.debug(myReturn);
      return myReturn;
    }
  }
}
