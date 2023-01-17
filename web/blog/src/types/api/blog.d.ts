export interface linkParams {
  linkName: string;
  linkAvatar: string;
  linkIntro: string;
  linkAddress: string;
}

export interface archive {
  id: number
  articleTitle: string
  createTime: Date
}

export interface tag {
  id: number
  tagName: string
}

export interface catagory {
  id: number
  categoryName: string
  articleCount: number
}

export interface link {
  id: number
  linkAddress: string
  linkAvatar: string
  linkName: string
  linkIntro: string
}