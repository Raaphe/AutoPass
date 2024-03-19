declare module '*.jpg';
declare module '*.jpeg';
declare module '*.gif';
declare module '*.svg';
declare module '*.ico';
declare module '*.webp';
declare module '*.avif';
declare module '*.png' {
    const src: string;
    export default src;
}
