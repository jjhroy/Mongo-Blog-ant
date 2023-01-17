interface websiteConfig {
    articleCover: string,
    websiteNotice: string,
    touristAvatar: string,
    userAvatar: string,
    websiteAuthor: string,
    websiteAvatar: string,
    websiteCover: string,
    websiteCreateTime: string,
    websiteIntro: string,
    websiteName: string,
    websiteRecordNo: string,
}

export interface blogInfo {
    viewsCount: string,
    articleCount: number,
    categoryCount: number,
    tagCount: number,
    websiteConfig: websiteConfig
}

export interface newCommnet{
    id:number,
    articleTitle:string,
    newCommentsCount:number,
}