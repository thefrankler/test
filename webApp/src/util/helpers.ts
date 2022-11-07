export function copy<T>(item: T): T {
    return JSON.parse(JSON.stringify(item)) as typeof item;
}