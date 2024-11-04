import {FC} from "react"
import {CalendarFixedDate} from "../../model/administration/CalendarEvent";
import {Form, InputGroup} from "react-bootstrap";
import Formatters from "../Formatters";


export type CalendarFixedDateEditorProps = {
    fixedDate: CalendarFixedDate,
    disabled?: boolean,
    onChange: (fixedDate: CalendarFixedDate) => void
}


const CalendarFixedDateEditor: FC<CalendarFixedDateEditorProps> = ({ fixedDate, onChange, disabled = false }) => {

    return <InputGroup className="mb-3">
        <InputGroup.Text>Jour</InputGroup.Text>
        <Form.Select aria-label="Default select example"
            value={fixedDate.day}
            disabled={disabled}
            onChange={(event) => onChange({ ...fixedDate, day: Number(event.target.value) })}
        >
            {
                Array.from({ length: 31 }, (_, i) => i + 1)
                    .map(index => <option value={index} key={index}>{Formatters.leadingZero(index)}</option>)
            }
        </Form.Select>
        <InputGroup.Text>Mois</InputGroup.Text>
        <Form.Select aria-label="Default select example"
            value={fixedDate.month}
            disabled={disabled}
            onChange={(event) => onChange({ ...fixedDate, month: Number(event.target.value) })}

        >
            {
                Array.from({ length: 12 }, (_, i) => i + 1)
                    .map(index => <option value={index} key={index}>{Formatters.leadingZero(index)}</option>)
            }
        </Form.Select>
    </InputGroup>
}

export default CalendarFixedDateEditor;