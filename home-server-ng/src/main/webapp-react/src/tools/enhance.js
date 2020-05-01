export default function enhance() {

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


    return {
        selectable: selectable
    };
}