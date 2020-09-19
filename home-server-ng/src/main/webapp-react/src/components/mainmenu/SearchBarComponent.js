import React, { useRef } from 'react';
import eventReactor from '../../eventReactor/EventReactor';
import { useState, useEffect } from 'react';
import { when } from '../../tools/when';

export function SearchBarComponent() {


    const [searchString, setSearchString] = useState("");
    const [displaySearchBar, setDisplaySearchBar] = useState(false);

    const searchInput = useRef(null);


    const onKeyDownHandler = (event) => {
        if (event.keyCode === 27) {
            searchSubReactor().clearSearchBar();
        }
    }

    useEffect(() => {




        const unsubscribeSearchEvent = searchSubReactor().onSearchEvent(newSearchString => setSearchString(newSearchString));
        const unsubscribeClearEvent = searchSubReactor().onClearSearchBar(() => setDisplaySearchBar(false));
        const unsubscribeDisplayEvent = searchSubReactor().onDisplaySearchBar(() => setDisplaySearchBar(true));


        return () => {
            unsubscribeSearchEvent();
            unsubscribeClearEvent();
            unsubscribeDisplayEvent();
        }


    }, []);

    useEffect(() => {
        if (displaySearchBar === true) {
            searchInput.current.focus();
        }
    }, [displaySearchBar]);


    return <nav className={when(!displaySearchBar).thenHideElement("red lighten-5")}><div className="nav-wrapper">
        <form>
            <div className="input-field">
                <input id="search" type="search" required
                    value={searchString}
                    onChange={(event) => searchSubReactor().searchEvent(event.target.value)}
                    onKeyDown={onKeyDownHandler}
                    ref={searchInput}
                >

                </input>
                <label className="label-icon" for="search"><i className="material-icons">search</i></label>
                <i className="material-icons" onClick={() => searchSubReactor().clearSearchBar()}>close</i>
            </div>
        </form>
    </div>
    </nav>
}

export function SearchDisplayButtonComponent() {
    const [displayButton, setDisplayButton] = useState(true);

    useEffect(() => {
        const unsubscribeClearEvent = searchSubReactor().onClearSearchBar(() => setDisplayButton(true));
        const unsubscribeDisplayEvent = searchSubReactor().onDisplaySearchBar(() => setDisplayButton(false));


        return () => {
            unsubscribeClearEvent();
            unsubscribeDisplayEvent();
        }

    }, []);


    return <a href="#!" className={when(!displayButton).thenHideElement()} onClick={() => searchSubReactor().displaySearchBar()}><i className="material-icons left">search</i></a>
}

export function searchSubReactor() {
    const searchEvent = (newSearchString) => eventReactor().emit("SEARCH_EVENT", newSearchString);
    const onSearchEvent = (eventHandler) => eventReactor().subscribe("SEARCH_EVENT", eventHandler);

    const displaySearchBar = () => eventReactor().emit("DISPLAY_SEARCH_BAR");
    const onDisplaySearchBar = (eventHandler) => eventReactor().subscribe("DISPLAY_SEARCH_BAR", eventHandler);

    const clearSearchBar = () => {
        searchEvent("");
        eventReactor().emit("CLEAR_SEARCH_BAR");
    }
    const onClearSearchBar = (eventHandler) => eventReactor().subscribe("CLEAR_SEARCH_BAR", eventHandler);

    return {
        searchEvent: searchEvent,
        onSearchEvent: onSearchEvent,
        displaySearchBar: displaySearchBar,
        onDisplaySearchBar: onDisplaySearchBar,
        clearSearchBar: clearSearchBar,
        onClearSearchBar: onClearSearchBar
    }
}