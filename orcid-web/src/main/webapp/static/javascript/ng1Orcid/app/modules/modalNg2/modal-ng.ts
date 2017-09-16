import * as angular 
    from 'angular';

import { NgModule } 
    from '@angular/core';

import { downgradeComponent, UpgradeModule } 
    from '@angular/upgrade/static';

import { ModalNgComponent } from './modal-ng.component.ts';

// This is the Angular 1 part of the module
export const ModalModule = angular.module(
    'ModalModule', 
    []
);
 
// This is the Angular 2 part of the module
@NgModule(
    {
        imports: [
        ],
        
        declarations: [ 
            ModalNgComponent
        ],
        entryComponents: [ 
            ModalNgComponent 
        ]/*,
        exports: [
            ModalNgComponent,
            ModalNg2Module
        ]*/
    }
)
export class ModalNg2Module {}

// components migrated to angular 2 should be downgraded here
//Must convert as much as possible of our code to directives

ModalModule.directive(
    'modalngcomponent', 
    <any>downgradeComponent(
        {
            component: ModalNgComponent
        }
    )
);
