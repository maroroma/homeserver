import { SearchRequest } from "../model/search/SearchRequest";
import type { SearchResponse } from "../model/search/SearchResponse";
import { RequesterUtils } from "./RequesterUtils";

export class SearchRequester {
    public static search(valueToSearch: string): Promise<SearchResponse> {
        return RequesterUtils.post("/api/musicplayer/search", new SearchRequest(valueToSearch))
    }
}