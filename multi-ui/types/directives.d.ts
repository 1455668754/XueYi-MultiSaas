import type { Directive } from 'vue';

declare module 'vue' {
  export interface ComponentCustomProperties {
    vLoading: Directive<Element, boolean>;
    vAuth: Directive<Element, string | string[]>;
  }
}

export {};
