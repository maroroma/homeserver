import { Drive } from './drive.modele';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'driveUsageColor'
})
export class DriveUsageColorPipe implements PipeTransform {
    transform(oneDrive: Drive): string {
        let returnValue = 'progress-bar progress-bar-';

        if (oneDrive.percentageUsed <= 70) {
            returnValue += 'success';
        }
        if (oneDrive.percentageUsed > 70 && oneDrive.percentageUsed < 90) {
            returnValue += 'warning';
        }
        if (oneDrive.percentageUsed >= 90) {
            returnValue += 'danger';
        }


        return returnValue;
    }
}
