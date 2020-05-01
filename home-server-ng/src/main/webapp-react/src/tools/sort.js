export default function sort() {


    const neutral = () => {
        return (item1, item2) => 0;
    }

    const basic = (extractor = (oneItem => oneItem)) => {
        return (item1, item2) => extractor(item1).localeCompare(extractor(item2));
    }

    const fileName = () => {
        return basic(oneFile => oneFile.name);
    }


    return {
        neutral: neutral,
        basic: basic,
        fileName: fileName
    };
}