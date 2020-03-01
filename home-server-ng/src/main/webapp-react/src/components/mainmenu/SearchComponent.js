import React from 'react';
import eventReactor from '../../eventReactor/EventReactor';
import { SEARCH_EVENT } from '../../eventReactor/EventIds';
import { useState } from 'react';
import IconComponent from '../commons/IconComponent';


export default function SearchComponent({ onSearchCloseHandler }) {


    const [searchText, setSearchText] = useState('');

    const onSearchTextChanged = (event) => {
        setSearchText(event.target.value);
        eventReactor().emit(SEARCH_EVENT, event.target.value);
    }

    const innerOnSearchCloseHandler = () => {
        setSearchText('');
        eventReactor().emit(SEARCH_EVENT, '');
        onSearchCloseHandler();
    }

    const onKeyDownHandler = (event) => {
        if (event.keyCode === 27) {
            innerOnSearchCloseHandler();
        }
    }



    return (
        <li>
            <form>
                <div className="input-field">
                    <input id="search" type="search" required onChange={onSearchTextChanged} value={searchText} onKeyDown={onKeyDownHandler}></input>
                    <label className="label-icon" htmlFor="search">
                        <IconComponent icon="search"></IconComponent>
                    </label>
                    <IconComponent icon="close" onClick={innerOnSearchCloseHandler}></IconComponent>
                    {/* <i className="material-icons" onClick={innerOnSearchCloseHandler}>close</i> */}
                </div>
            </form>
        </li>
    )
}