import type { ThumbSize } from "../thumb/ThumbProps";

export type ListItemRendererProps = {
  label: string;
  thumb?: React.ReactElement;
  onClick?: () => void;
  downloadLink?: string;
  enableDownload?: boolean;
  downloadTitle?: string;
  size?: ThumbSize;
};