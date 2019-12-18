import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Button, ExpansionPanelActions, Grid } from '@material-ui/core';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import DeleteIcon from '@material-ui/icons/Delete';
import AddAPhotoIcon from '@material-ui/icons/AddAPhoto';
import PersonAddDisabledIcon from '@material-ui/icons/PersonAddDisabled';
import IconButton from '@material-ui/core/IconButton'

const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    flexBasis: '33.33%',
    flexShrink: 0,
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(15),
    color: theme.palette.text.secondary,
  },
}));

const array = [...Array(20).keys()];
const date = new Date();

export default function ControlledExpansionPanels() {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false);

  const handleChange = panel => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : false);
  };
  
  return (

    <div className={classes.root}>
      {
        array.map(i => (
          <ExpansionPanel expanded={expanded === `panel${i}`} onChange={handleChange(`panel${i}`)}>
            <ExpansionPanelSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls={`panel${i}bh-content`}
              id={`panel${i}bh-header`}>
              <Typography className={classes.heading}>{new Date(date.setDate(date.getDate() - 7)).toLocaleString()}</Typography>
              <Typography className={classes.secondaryHeading}>Lecture {i}</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails>
              <List>
                {
                  array.map(item => (

                    <ListItem>
                          <ListItemText primary={`Studente ${item}`}/>
                          <IconButton edge="end" aria-label="delete"> <PersonAddDisabledIcon /> </IconButton>
                    </ListItem>

                  ))
                }
              </List>
            </ExpansionPanelDetails>
            <ExpansionPanelActions>
              <IconButton onClick={{}} component="label"><AddAPhotoIcon /><input type="file" style={{ display: "none" }} onChange={{}} accept=".jpg,.jpeg,.png,.bmp" /></IconButton>
              <IconButton onClick={{}}><PersonAddIcon /></IconButton>
              <IconButton onClick={{}}><DeleteIcon style={{ color: "#C10000" }} /></IconButton>
            </ExpansionPanelActions>
          </ExpansionPanel>
        ))
      }

    </div>
  );
}