    import React from 'react'
    import Card from '@material-ui/core/Card'
    import CardMedia from '@material-ui/core/CardMedia'
    import Typography from '@material-ui/core/Typography'
    import { useStyles } from './HomeStyle.js'
    import Grid from '@material-ui/core/Grid'
    import Divider from '@material-ui/core/Divider'
    import TabPanel from './TabPanel'
    import BaseInstance from '../http-client/BaseInstance'
    import CourseCreation from './CourseCreation'
    import Cookies from 'js-cookie'
    import { createBrowserHistory } from "history"
    import Fab from '@material-ui/core/Fab'
    import Mail from '@material-ui/icons/Mail'
    import AccountCircle from '@material-ui/icons/AccountCircle'
    import AddAPhotoIcon from '@material-ui/icons/AddAPhoto'
    import Tooltip from '@material-ui/core/Tooltip'
    import { Avatar } from '@material-ui/core'

    export default function RecipeReviewCard({ history }) {

        if (history === undefined) {
            history = createBrowserHistory()
            history.push("/login")
        }

        if (Cookies.get("email") === undefined)
            history.push("/login");

        const classes = useStyles();
        const [expanded, setExpanded] = React.useState(false);

        const [courses, setCourses] = React.useState(JSON.parse(Cookies.get("createdCourses")))

        var imagePath = Cookies.get("profilePicture"); 
        if (imagePath === "null") 
            imagePath = BaseInstance.defaults.baseURL + 'uploads/profilePictures/default.png'
        else 
            imagePath = BaseInstance.defaults.baseURL + imagePath

        const onFileChangeHandler = (e) => {
            e.preventDefault();
            const formData = new FormData();
            var nameFile = e.target.files[0].name;
            var extension = nameFile.match(/.*\.(\w+)/)[1];
            nameFile = Cookies.get("email") + "." + extension.toLowerCase();
            formData.append('file', e.target.files[0], nameFile);
            BaseInstance.post("updateProfilePicture", formData)
                .then(res => {
                    if (res.data === "Wrong extension") {
                        alert("wrong extension")
                        return
                    }
                    if (res.data === "Failed") {
                        alert("generic error")
                        return
                    }
                    imagePath = res.data
                    Cookies.set("profilePicture", imagePath)
                    window.location.reload(true);
                })
        };

        return (
            <div className={classes.profileContainer} style={{ padding: 20 }}>
                <Grid container>
                <Grid item xs={3}>

                <div style={{"margin-bottom" : "-40px"}}>
                    <Avatar alt="profile image" src={imagePath} style={{width: "60%", height: "auto", "marginLeft": "20%"}} />
                    <Tooltip title="Update profile picture" >
                        <Fab component="label" style={{"position":"relative", "bottom":"40px", "marginLeft":"65%", "backgroundColor":"#4151a7"}}>
                            <AddAPhotoIcon style={{ color: "white" }}/>
                            <input type="file" style={{ display: "none" }} onChange={onFileChangeHandler}
                                    accept=".jpg,.jpeg,.png,.bmp" />
                        </Fab>
                    </Tooltip>
                </div>

                <Divider style={{ margin: "20px"}} />

                <Card className={classes.summaryCard}>
                    <div style={{ display: "flex", "justify-content": "space-between", padding:"10px" }}>
                        <AccountCircle color="action" font-size="small"/>
                        <Typography className={classes.summaryField}> <b>{Cookies.get("firstName")}</b> <b>{Cookies.get("lastName")}</b></Typography>
                    </div>
                    <Divider></Divider>
                    <div style={{ display: "flex", "justify-content": "space-between", padding:"10px"}}>
                        <Mail color="action" font-size="small"/>
                        <Typography className={classes.summaryField}><b>{Cookies.get("email")}</b></Typography>
                    </div>
                </Card>

                <div style={{"padding":"5%"}}>
                    <CourseCreation courses={courses} setCourses={setCourses}/>
                </div>

                </Grid>
                    <Grid item xs={9} style={{ paddingLeft: "20px" }}>
                        <Card className={classes.coursesCard}>
                            <TabPanel courses={courses} setCourses={setCourses}/>
                        </Card>
                    </Grid>
                </Grid>
            </div>
        );
    }