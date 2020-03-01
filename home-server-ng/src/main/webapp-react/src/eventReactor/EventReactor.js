const eventReactorSubscription = {};

export default function eventReactor() {
    const subscribe = (eventId, eventListener) => {
        const existingEventListeners = eventReactorSubscription[eventId] !== undefined ? eventReactorSubscription[eventId] : [];
        eventReactorSubscription[eventId] = [...existingEventListeners, eventListener];
        return () => eventReactorSubscription[eventId] = [...existingEventListeners];
    }

    const emit = (eventId, data) => {
        if (eventReactorSubscription[eventId] !== undefined) {
            eventReactorSubscription[eventId].forEach(oneEventHandler => oneEventHandler(data));
        }
    }

    return {
        subscribe: subscribe,
        emit: emit
    }
}

