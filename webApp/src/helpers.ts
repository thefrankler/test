export function copy(item: any): any {
    if (!item) { return item; }

    var types = [ Number, String, Boolean ],
        result: any;

    types.forEach(function(type) {
        if (item instanceof type) {
            result = type( item );
        }
    });

    if (typeof result == "undefined") {
        if (Object.prototype.toString.call( item ) === "[object Array]") {
            result = [];
            item.forEach(function(child: any, index: string | number, array: any) {
                result[index] = copy( child );
            });

        } else if (typeof item == "object") {
            if (item.nodeType && typeof item.cloneNode == "function") {
                result = item.cloneNode( true );
            } else if (!item.prototype) {
                if (item instanceof Date) {
                    result = new Date(item);
                } else {
                    result = {};
                    for (var i in item) {
                        result[i] = copy( item[i] );
                    }
                }

            } else {
                if (false && item.constructor) {
                    result = new item.constructor();
                } else {
                    result = item;
                }
            }
        } else {
            result = item;
        }
    }

    return result;
}