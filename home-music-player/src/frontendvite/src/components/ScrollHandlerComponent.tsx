import {type FC, useEffect} from "react";
import {useMusicPlayerContext} from "../state/MusicPlayerContext";
import {ScrollAction} from "../state/actions/ScrollAction";

const ScrollHandlerComponent: FC = () => {
  const { dispatch } = useMusicPlayerContext();

  const handleScroll = () => {
    dispatch(new ScrollAction(window.pageYOffset));
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll, { passive: true });

    return () => window.removeEventListener("scroll", handleScroll);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return <></>;
};

export default ScrollHandlerComponent;
