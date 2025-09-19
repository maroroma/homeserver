import type {RouteObject} from "react-router";

/* eslint-disable @typescript-eslint/no-explicit-any */
export default class Path {
    constructor(private path: string, private componentProvider: () => React.ReactNode, private pathParams: string[] = []) { }


    toRoute(): RouteObject {
        return {
            path: this.path,
            element: this.componentProvider()
        }
    }


    resolve(params: any[] | any = []): string {

        if (params instanceof Array) {
            const paramsAsObjects = params.map((aParam, index) => {
                const rawObject: any = {};
                rawObject[this.pathParams[index]] = aParam;
                return rawObject;
            });


            return this.innerResolve(paramsAsObjects);
        }

        const rawObject: any = {};
        rawObject[this.pathParams[0]] = params;

        return this.innerResolve([
            rawObject
        ])


    }


    private innerResolve(params: any[] = []): string {

        let pathToTransform = this.path;

        params.forEach(aParamToResolve => {
            pathToTransform = pathToTransform.replace(`:${Object.keys(aParamToResolve)[0]}`, `${Object.values(aParamToResolve)[0]}`)
        });

        return pathToTransform;
    }
}