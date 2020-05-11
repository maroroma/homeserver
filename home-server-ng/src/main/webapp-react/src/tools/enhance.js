export default function enhance() {

    const disablable = () => {
        return oneItem => {
            const disableItem = {
                ...oneItem,
                disabled: false
            };

            const enable = () => {
                disableItem.disabled = false;
                return disableItem;
            }

            const disable = () => {
                disableItem.disabled = true;
                return disableItem;
            }

            disableItem.enable = enable;
            disableItem.disable = disable;
            return disableItem;
        }
    }


    const selectable = () => {
        return oneItem => {
            const selectableItem = {
                ...oneItem,
                selected: false
            };

            const toggle = (newStatus) => {
                if (newStatus !== undefined) {
                    selectableItem.selected = newStatus
                } else {
                    selectableItem.selected = !selectableItem.selected;
                }
                return selectableItem;
            };

            selectableItem.toggle = toggle;

            return selectableItem;
        };
    }

    const indexed = () => {
        return (oneItem, index) => {
            return {
                ...oneItem,
                index: index
            }
        }
    }

    return {
        selectable: selectable,
        disablable: disablable,
        indexed: indexed
    };
}