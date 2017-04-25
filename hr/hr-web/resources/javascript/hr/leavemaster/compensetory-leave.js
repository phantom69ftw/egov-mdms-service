class CompensetoryLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      employees:[],
      compensetorySet:{
      workedOn:"",
      compensatoryForDate:"",
      name:"",
      code:""},}
    this.handleChange=this.handleChange.bind(this);

  }


  handleChange(e,name)
  {
      this.setState({
          compensetorySet:{
              ...this.state.compensetorySet,
              [name]:e.target.value
          }
      })
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }



  Add(e)
  {
    e.preventDefault();
    console.log(this.state.compensetorySet);
    this.setState({compensetorySet:{
       workedOn:"",
       compensatoryForDate:"",
       name:"",
       code:""
     }})
  }

  componentDidMount(){
    var type = getUrlVars()["type"], _this = this, id = getUrlVars()["id"];
    $('#compensatoryForDate').datepicker({
        format: 'dd/mm/yyyy',
        autclose:true

    });
    $('#compensatoryForDate').on("change", function(e) {

      _this.setState({
            compensetorySet: {
                ..._this.state.compensetorySet,
                "compensatoryForDate":$("#compensatoryForDate").val()
            }
      })
      });
      if (!id) {

       var obj = commonApiPost("hr-employee","employees","_loggedinemployee",{tenantId}).responseJSON["Employee"][0];
      } else {
        var obj = getCommonMasterById("hr-employee","employees","Employee",id).responseJSON["Employee"][0];
      }

      _this.setState({
        compensetorySet:{
            ..._this.state.compensetorySet,
            name:obj.name,
            code:obj.code,
            employee:obj.id,
            // workflowDetails:{
            //   ..._this.state.leaveSet.workflowDetails,
            //   assignee: assignee || ""
            // }

          }
      })
    }





  render() {
      let {handleChange}=this;
      let {code,name,workedOn,compensatoryForDate}=this.state.compensetorySet;
      let mode=getUrlVars()["type"];

      const showActionButton=function() {
        if((!mode) ||mode==="create")
        {
          return (<button type="submit" className="btn btn-submit">{mode?"Apply":"forward"}</button>);
        }
      };
    return (
      <div>
          <h3>{mode} Compensetory Leave</h3>
            <form onSubmit={(e)=>{this.Add(e)}}>
          <fieldset>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code </label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code}
                              onChange={(e)=>{  handleChange(e,"code")}}required/>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
                          </div>
                          <div className="col-sm-6">
                              <input  type="text" id="name" name="name" value={name}
                              onChange={(e)=>{  handleChange(e,"name")}}required/>

                            </div>
                          </div>
                        </div>
                    </div>



                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Worked On  </label>
                          </div>
                          <div className="col-sm-6">
                          <input  type="text" id="workedOn" name="workedOn" value={workedOn}
                          onChange={(e)=>{  handleChange(e,"workedOn")}}required/>
                      </div>
                    </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Compensetory Leave On  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text" id="compensatoryForDate" name="compensatoryForDate" value={compensatoryForDate}
                          onChange={(e)=>{  handleChange(e,"compensatoryForDate")}}required/>

                          </div>
                          </div>
                        </div>
                      </div>
                </div>





            <div className="text-center">
                 <button type="submit"  className="btn btn-submit">Forward</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
          </fieldset>
          </form>
      </div>
    );
  }
}




ReactDOM.render(
  <CompensetoryLeave />,
  document.getElementById('root')
);
